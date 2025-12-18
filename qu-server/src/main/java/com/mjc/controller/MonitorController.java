package com.mjc.controller;

import com.mjc.Result.Result;
import com.mjc.entity.Server;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/monitor")
@Tag(name = "系统监控接口")
public class MonitorController {

    @GetMapping("/server")
    @Operation(summary = "获取服务器信息")
    public Result<Server> getServerInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return Result.success(server);
    }
}