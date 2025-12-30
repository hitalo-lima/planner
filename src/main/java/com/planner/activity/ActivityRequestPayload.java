package com.planner.activity;

import java.time.LocalDateTime;

public record ActivityRequestPayload(String title, LocalDateTime occursAt) {

}
