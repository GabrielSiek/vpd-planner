package com.vpd.ApiResponse;

import com.vpd.Friendship.FriendshipDTO;
import org.springframework.http.HttpStatus;

public class ApiResponseHelper {

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, HttpStatus.OK.value());
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(true, message, data, HttpStatus.CREATED.value());
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, message, null, HttpStatus.NOT_FOUND.value());
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(false, message, null, HttpStatus.BAD_REQUEST.value());
    }

    public static <T> ApiResponse<T> unauthorized() {
        return new ApiResponse<>(false, "Unauthorized", null, HttpStatus.UNAUTHORIZED.value());
    }

    public static <T> ApiResponse<T> internalError(Exception exception) {
        return new ApiResponse<>(false, "Internal Error: " + exception.getMessage(), null,HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static <T> ApiResponse<T> noContent(String message) {
        return new ApiResponse<>(false, message, null, HttpStatus.NO_CONTENT.value());
    }
}
