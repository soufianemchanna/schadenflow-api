package com.sosmoch.schadenflow.agent.internal;


import com.sosmoch.schadenflow.agent.AgentWorkloadPort;
import com.sosmoch.schadenflow.agent.dto.AgentView;
import com.sosmoch.schadenflow.agent.dto.CreateAgentRequest;
import com.sosmoch.schadenflow.agent.dto.UpdateAgentRequest;
import com.sosmoch.schadenflow.agent.exceptions.AgentHasOpenClaimsException;
import com.sosmoch.schadenflow.agent.exceptions.AgentNotFoundException;
import com.sosmoch.schadenflow.agent.exceptions.IdentityCheckException;
import com.sosmoch.schadenflow.auth.AuthFacade;
import com.sosmoch.schadenflow.user.CreateUserCommand;
import com.sosmoch.schadenflow.user.UserFacade;
import com.sosmoch.schadenflow.user.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final UserFacade userFacade;
    private final AuthFacade authFacade;
    private final AgentWorkloadPort agentWorkloadPort;

    @Transactional
    public AgentView createAgent(CreateAgentRequest request){
        // check if email is already registered
        if(userFacade.findUserByEmail(request.email()).isPresent()){
            throw new IdentityCheckException("Email is already registered");
        }
        // create user
        CreateUserCommand userCommand = new CreateUserCommand(
                request.email(),
                authFacade.passwordEncoder().encode(request.inialPassword()),
                com.sosmoch.schadenflow.user.Role.AGENT
        );
        UserView userView = userFacade.createUser(userCommand);
        // create agent
        Agent agent = Agent.builder()
                .agentName(request.agentName())
                .email(request.email())
                .specialization(request.specialization())
                .userId(userView.id())
                .agentNumber(null)
                .build();

        return AgentView.from(agentRepository.save(agent), agentWorkloadPort.countActiveClaims(agent.getId()));
    }

    public Page<AgentView> getAll(Boolean available, Specialization specialization, Pageable pageable){
        Specification<Agent> spec = Specification.unrestricted();

        if(available != null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("available"), available));
        }
        if(specialization != null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("specialization"), specialization));
        }

        Page<Agent> agents = agentRepository.findAll(spec, pageable);
        Map<UUID, Long> counts = agentWorkloadPort.countActiveClaims(agents.map(Agent::getId).getContent());

        return agents.map(agent -> {
            Long activeClaims = counts.get(agent.getId());
            return AgentView.from(agent, activeClaims);
        });
    }

    public AgentView getById(UUID id){
        Agent agent = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException(id.toString()));
        return AgentView.from(agent, agentWorkloadPort.countActiveClaims(agent.getId()));
    }

    public AgentView toggleAvailability(UUID id) {
        Agent agent = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException(id.toString()));
        agent.setAvailable(!agent.isAvailable());
        return AgentView.from(agentRepository.save(agent), agentWorkloadPort.countActiveClaims(agent.getId()));
    }

    public void deleteAgent(UUID id) {
        Agent agent = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException(id.toString()));
        if(agentWorkloadPort.countActiveClaims(agent.getId()) > 0){
            throw new AgentHasOpenClaimsException();
        }
        agent.setActive(false);
        agentRepository.delete(agent);
    }

    public AgentView updateAgent(UUID id, UpdateAgentRequest request) {
        Agent agent = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException(id.toString()));
        agent.setAgentName(request.agentName());
        agent.setSpecialization(request.specialization());
        return AgentView.from(agentRepository.save(agent), agentWorkloadPort.countActiveClaims(agent.getId()));
    }

    @Transactional
    public AgentView toggleActivity(UUID id) {
        Agent agent = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException(id.toString()));

        if(agent.isActive()){
            if(agentWorkloadPort.countActiveClaims(agent.getId()) > 0){
                throw new AgentHasOpenClaimsException();
            }
            userFacade.disableUser(agent.getUserId());
        }
        else{
            userFacade.enableUser(agent.getUserId());
        }
        agent.setActive(!agent.isActive());
        return AgentView.from(agentRepository.save(agent), agentWorkloadPort.countActiveClaims(agent.getId()));
    }
}
