package gguip1.community.domain.policy.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PolicyPageResponse(String title, String description, LocalDateTime effectiveDate, List<PolicySections> sections,
                                 ContactInfo contactInfo) {
}
