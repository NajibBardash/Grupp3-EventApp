package se.yrgo.user_service.controller;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClient;
import se.yrgo.user_service.data.UserRepository;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private UserRepository data;
    private final RestClient restClient;
    private LoadBalancerClient balancer;

    public UserController(UserRepository data, RestClient.Builder restClientBuilder, LoadBalancerClient balancer) {
        this.data = data;
        this.restClient = restClientBuilder.build();
        this.balancer = balancer;
    }


    // TODO: Function: Create User

    // TODO: Function: Delete User

    // TODO: Function: List All Users.

    // TODO: Function: Show User By customerId

    // TODO: Function: Show User By Name
}
