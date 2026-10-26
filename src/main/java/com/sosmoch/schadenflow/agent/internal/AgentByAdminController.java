package com.sosmoch.schadenflow.agent.internal;


import com.sosmoch.schadenflow.agent.dto.AgentView;
import com.sosmoch.schadenflow.agent.dto.CreateAgentRequest;
import com.sosmoch.schadenflow.agent.dto.UpdateAgentRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/agents")
@Tag(name = "Admin / Agent", description = "Agent operations")
@RequiredArgsConstructor
public class AgentByAdminController {
    private final AgentService agentService;

    @PostMapping("/create-agent")
    public AgentView createAgent(@Valid @RequestBody CreateAgentRequest request){
        return agentService.createAgent(request);
    }

    @GetMapping
    public Page<AgentView> list(
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Specialization specialization,
            @PageableDefault(size = 20, sort = "agentName") Pageable pageable
    ){
        return agentService.getAll(available, specialization, pageable);
    }

    @GetMapping("/{id}")
    public AgentView getAgent(@PathVariable UUID id){
        return agentService.getById(id);
    }

    @PatchMapping("/{id}/availability")
    public AgentView updateAvailability(@PathVariable UUID id){
        return agentService.toggleAvailability(id);
    }

    @PatchMapping("/{id}/activate")
    public AgentView updateActive(@PathVariable UUID id){
        return agentService.toggleActivity(id);
    }

    @PatchMapping("/{id}")
    public AgentView updateAgent(@PathVariable UUID id, @RequestBody UpdateAgentRequest request){
        return agentService.updateAgent(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAgent(@PathVariable UUID id){
        agentService.deleteAgent(id);
    }

}
