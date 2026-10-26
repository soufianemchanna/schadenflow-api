

@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"infrastructure::storage-api", "core", "agent"}
)
package com.sosmoch.schadenflow.claim;