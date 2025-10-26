package gguip1.community.global.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PolicySections(String title, List<String> contents) {
}
