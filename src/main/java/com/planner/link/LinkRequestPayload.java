package com.planner.link;

import io.swagger.v3.oas.annotations.media.Schema;

public record LinkRequestPayload(@Schema(description = "Title of the link", example = "Official Website") String title,
        @Schema(description = "URL of the link", example = "https://www.officialwebsite.com") String url) {

}
