package vsla.group.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import vsla.group.Group;
import vsla.userManager.user.Users;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {
    private GroupResponse groupInfo;
    private GenderStatics genderStatics;
    private List<Member> memberList;

    @Data
    @Builder
    static
    class Member {
        private Long userId;
        private String fullName;
        private String gender;
        private Boolean proxy;
        private Integer round;
        private String phoneNumber;
        private Double totalOwning;
        private Double loanBalance;
        private Double paid;
    }

    @Data
    static
    class GenderStatics {
        private int male;
        private int female;
    }

    public static MemberResponse toResponse(List<Users> users) {
        Group group = users.get(0).getGroup();
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setGroupInfo(groupResponse(group));
        memberResponse.setGenderStatics(genderStatics(users));
        memberResponse.setMemberList(members(users));
        return memberResponse;
    }

    private static GroupResponse groupResponse(Group group) {
        return GroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .groupSize(group.getGroupSize())
                .entryFee(group.getEntryFee())
                .address(group.getAddress())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }


    private static GenderStatics genderStatics(List<Users> users) {

        int totalUsers = users.size();
        int male = 0;

        for (Users user : users)
            if (user.getGender().equalsIgnoreCase("male"))
                male += 1;

        GenderStatics genderStatics = new GenderStatics();
        genderStatics.setMale(male);
        genderStatics.setFemale(totalUsers - male);

        return genderStatics;
    }

    private static List<Member> members(List<Users> users) {
        List<Member> members = new ArrayList<>();
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.##"); // Format to two decimal places

        for (Users user : users) {
            double totalOwning = 5000 + (300 * random.nextDouble()); // Generates a value between 5000 and 800
            double loanBalance = 5000 - 300 * random.nextDouble(); // Generates a value between 300 and 5000
            double paid = 5000 - (300 * random.nextDouble()); // Generates a value between 300 and 5000

            if (totalOwning < loanBalance) {
                totalOwning = loanBalance;
            }
            if (loanBalance < paid) {
                loanBalance = paid;
            }
            
            Member member = Member.builder()
                    .userId(user.getUserId())
                    .fullName(user.getFullName())
                    .gender(user.getGender())
                    .proxy(user.getProxyEnabled())
                    .phoneNumber(user.getUsername())
                    .totalOwning(Double.parseDouble(df.format(totalOwning)))
                    .loanBalance(Double.parseDouble(df.format(loanBalance)))
                    .paid(Double.parseDouble(df.format(paid)))
                    .build();

            members.add(member);
        }
        return members;
    }
}
