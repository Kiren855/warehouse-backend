package com.sunny.scm.grpc_common.client;

import identity.AuthorizationServiceGrpc;
import identity.AuthorizationServiceOuterClass.CheckPermissionRequest;
import identity.AuthorizationServiceOuterClass.CheckPermissionResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationClient {
    private final AuthorizationServiceGrpc.AuthorizationServiceBlockingStub authStub;

    public AuthorizationClient(@GrpcClient("identity-service") AuthorizationServiceGrpc.AuthorizationServiceBlockingStub authStub) {
        this.authStub = authStub;
    }

    public boolean checkPermission(String userId, String permission) {
        CheckPermissionRequest request = CheckPermissionRequest.newBuilder()
                .setUserId(userId)
                .setRole(permission)
                .build();

        CheckPermissionResponse response = authStub.checkPermission(request);
        return response.getAllowed();
    }
}
