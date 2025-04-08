package top.aprdec.onepractice.entity.voteentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteStatisticsEntity {
    private Integer number;
//    计算出来的分数
    private Integer ratingnow;
}
