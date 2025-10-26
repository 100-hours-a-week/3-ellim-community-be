package gguip1.community.global.service;

import gguip1.community.global.response.ContactInfo;
import gguip1.community.global.response.PolicyPageResponse;
import gguip1.community.global.response.PolicySections;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PageService {

    public PolicyPageResponse getTermsContent() {
        String title = "이용약관";
        String description = "본 약관은 아무말 대잔치(이하 “서비스”)의 이용 조건과 절차, 회원과 서비스 제공자의 권리·의무 및 책임사항을 규정합니다.";
        LocalDateTime effectiveDate = LocalDateTime.of(2025, 10, 25, 0, 0);

        List<PolicySections> sections = List.of(
                PolicySections.builder()
                        .title("제1조 (목적 및 정의)")
                        .contents(List.of("서비스는 커뮤니티 기반의 게시 콘텐츠 공유와 소통을 목적으로 하며, “회원”은 약관에 동의하고 계정을 생성하여 서비스를 이용하는 개인을 말합니다."))
                        .build(),
                PolicySections.builder()
                        .title("제2조 (약관의 효력과 변경)")
                        .contents(List.of("약관은 서비스 화면에 게시하거나 기타 방법으로 공지함으로써 효력이 발생합니다.",
                                "법령 개정 또는 서비스 개선을 위해 변경될 수 있으며, 변경 시 최소 7일 전에 공지합니다."))
                        .build(),
                PolicySections.builder()
                        .title("제3조 (계정 생성 및 관리)")
                        .contents(List.of("회원은 정확한 정보를 제공하여 계정을 생성해야 하며, 계정 정보의 변경이 있을 경우 즉시 수정해야 합니다.",
                                "회원은 자신의 계정과 비밀번호를 안전하게 관리할 책임이 있으며, 제3자에게 양도하거나 대여할 수 없습니다."))
                        .build(),
                PolicySections.builder()
                        .title("제4조 (서비스 이요)")
                        .contents(List.of("회원은 서비스를 개인적이고 비상업적인 목적으로만 이용할 수 있습니다.",
                                "서비스 이용 시 관련 법령과 본 약관을 준수해야 합니다."))
                        .build(),
                PolicySections.builder()
                        .title("제5조 (데이터 보존 및 삭제)")
                        .contents(List.of("회원 탈퇴 시 계정과 게시 데이터는 30일간 안전 보관 후 완전 익명화되어 복구가 불가합니다.",
                                "회원 또는 게시물에 연결되지 않은 이미지 리소스는 7일 주기 자동 정리를 통해 완전 삭제합니다."))
                        .build(),
                PolicySections.builder()
                        .title("제6조 (이용 제한)")
                        .contents(List.of("불법 정보 유통, 타인 권리 침해, 서비스 안정성 저해 행위가 확인되면 사전 통보 없이 이용을 제한할 수 있습니다.",
                                "관련 법령 또는 이용 정책을 위반해 발생한 모든 책임은 회원 본인에게 있습니다."))
                        .build(),
                PolicySections.builder()
                        .title("제7조 (서비스 변경 및 중단)")
                        .contents(List.of("운영상·기술적 필요에 따라 서비스의 전부 또는 일부를 수정·중단할 수 있으며, 중단 시 사전 공지를 원칙으로 합니다. 긴급 장애, 법적 금지 등 불가피한 경우 사후 안내할 수 있습니다."))
                        .build(),
                PolicySections.builder()
                        .title("제8조 (면책 조항)")
                        .contents(List.of("서비스는 천재지변, 네트워크 장애, 회원 귀책 등 불가항력으로 인한 손해에 대해 책임을 지지 않습니다. 또한 회원 상호 간 분쟁에 개입하지 않으며, 필요한 경우 사법기관 요청에 따라 자료를 제공할 수 있습니다."))
                        .build(),
                PolicySections.builder()
                        .title("제9조 (관할)")
                        .contents(List.of("약관과 서비스 이용과 관련하여 분쟁이 발생할 경우 대한민국 법을 적용하며, 민사소송법상의 관할 법원을 전속 관할로 합니다."))
                        .build()
        );

        ContactInfo contactInfo = ContactInfo.builder()
                .email("gguip7554@naver.com")
                .phone("010-1234-5678")
                .build();

        return PolicyPageResponse.builder()
                .title(title)
                .description(description)
                .effectiveDate(effectiveDate)
                .sections(sections)
                .contactInfo(contactInfo)
                .build();
    }

    public PolicyPageResponse getPrivacyContent() {
        String title = "개인정보 처리방침";
        String description = "아무말 대잔치(이하 “서비스”)는 회원님의 개인정보와 게시 데이터를 안전하게 보호하기 위해 다음과 같은 원칙을 따릅니다.";
        LocalDateTime effectiveDate = LocalDateTime.of(2025, 10, 25, 0, 0);

        List<PolicySections> sections = List.of(
                PolicySections.builder()
                        .title("제1조 (수집하는 개인정보 항목)")
                        .contents(List.of("회원 가입 시 이메일 주소와 비밀번호를 수집합니다.",
                                "게시물 작성 시 게시 내용과 첨부된 이미지 데이터를 수집합니다."))
                        .build(),
                PolicySections.builder()
                        .title("제2조 (개인정보 수집 및 이용 목적)")
                        .contents(List.of("회원 관리, 서비스 제공 및 개선, 맞춤형 콘텐츠 제공을 위해 개인정보를 이용합니다.",
                                "법적 의무 준수를 위해 필요한 경우 개인정보를 처리할 수 있습니다."))
                        .build(),
                PolicySections.builder()
                        .title("제3조 (개인정보 보유 및 이용 기간)")
                        .contents(List.of("회원 탈퇴 시 계정과 게시 데이터는 30일간 안전 보관 후 완전 익명화되어 복구가 불가합니다.",
                                "회원 또는 게시물에 연결되지 않은 이미지 리소스는 7일 주기 자동 정리를 통해 완전 삭제합니다."))
                        .build(),
                PolicySections.builder()
                        .title("제4조 (개인정보 보호 조치)")
                        .contents(List.of("개인정보는 암호화하여 저장하며, 접근 권한이 있는 인원은 최소화합니다.",
                                "정기적인 보안 점검과 시스템 업데이트를 통해 개인정보 보호에 최선을 다하고 있습니다."))
                        .build(),
                PolicySections.builder()
                        .title("제5조 (개인정보 제3자 제공)")
                        .contents(List.of("법령에 따른 경우를 제외하고는 회원의 동의 없이 개인정보를 제3자에게 제공하지 않습니다."))
                        .build(),
                PolicySections.builder()
                        .title("제6조 (회원의 권리와 행사 방법)")
                        .contents(List.of("회원은 언제든지 자신의 개인정보 열람, 수정, 삭제를 요청할 수 있습니다.",
                                "개인정보 관련 문의는 아래 연락처로 연락해 주시기 바랍니다."))
                        .build()
        );

        ContactInfo contactInfo = ContactInfo.builder()
                .email("gguip7554@naver.com")
                .phone("010-1234-5678")
                .build();

        return PolicyPageResponse.builder()
                .title(title)
                .description(description)
                .effectiveDate(effectiveDate)
                .sections(sections)
                .contactInfo(contactInfo)
                .build();
    }
}
