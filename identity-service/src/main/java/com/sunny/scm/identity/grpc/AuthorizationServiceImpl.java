package com.sunny.scm.identity.grpc;

import com.sunny.scm.identity.service.IdentityService;
import identity.AuthorizationServiceGrpc;
import identity.AuthorizationServiceOuterClass.CheckPermissionRequest;
import identity.AuthorizationServiceOuterClass.CheckPermissionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class AuthorizationServiceImpl extends AuthorizationServiceGrpc.AuthorizationServiceImplBase {
    private final IdentityService identityService;
    @Override
    public void checkPermission(CheckPermissionRequest request,
                                StreamObserver<CheckPermissionResponse> responseObserver) {

        String userId = request.getUserId();
        String roleName = request.getRole();

        boolean allowed = identityService.checkPermission(userId, roleName);

        CheckPermissionResponse response = CheckPermissionResponse.newBuilder()
                .setAllowed(allowed)
                .setMessage(allowed ? "" : "User does not have the required permission")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
