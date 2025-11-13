package gguip1.community.domain.policy.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PolicySections(String title, List<String> contents) {
}
