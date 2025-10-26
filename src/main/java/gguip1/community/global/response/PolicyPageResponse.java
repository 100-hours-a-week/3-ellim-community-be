package gguip1.community.global.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PolicyPageResponse(String title, String description, LocalDateTime effectiveDate, List<PolicySections> sections,
                                 ContactInfo contactInfo) {
}
