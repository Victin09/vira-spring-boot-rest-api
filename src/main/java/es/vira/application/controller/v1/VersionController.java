package es.vira.application.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "API Info")
@RestController
@RequestMapping("/api/v1")
public class VersionController {

    @Value("${application.options.apiVersion}")
    private String apiVersion;

    @ApiOperation("Returns the API version.")
    @GetMapping(value = "/version")
    public String version() {
        return apiVersion;
    }
}
