package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.catstack.nfc_terminal.model.enums.UserStatus;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.UserService;

@RestController
@RequestMapping("/api/users/")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse getAboutMe() {
        var me = userService.getLoggedInUser();
        return new ApiResponse(me);
    }

    @GetMapping("/get")
    public ApiResponse getUsersGap(@RequestParam int from, @RequestParam int count) throws InterruptedException {
        var applications = userService.getUsersGap(from, count);
        return new ApiResponse(applications);
    }

    @GetMapping("/ban")
    public ApiResponse banUser(@RequestParam long id) {
        userService.updateStatusById(id, UserStatus.LOCKED);
        return new ApiResponse("User banned successfully");
    }

    @GetMapping("/delete")
    public ApiResponse deleteUser(@RequestParam long id) {
        userService.updateStatusById(id, UserStatus.DELETED);
        return new ApiResponse("User deleted successfully");
    }


    @GetMapping("/activate")
    public ApiResponse activateUser(@RequestParam long id) {
        userService.updateStatusById(id, UserStatus.ACTIVE);
        return new ApiResponse("User activate successfully");
    }
}
