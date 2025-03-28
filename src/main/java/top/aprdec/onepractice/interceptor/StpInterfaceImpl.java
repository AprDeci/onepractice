package top.aprdec.onepractice.interceptor;

import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.dto.resp.UserInfoRespDTO;
import top.aprdec.onepractice.eenum.UserTypeEnum;
import top.aprdec.onepractice.service.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserService userService;

    @Override
    public List<String> getPermissionList(Object LongId, String loginType) {
        
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object LongId, String loginType) {
        UserInfoRespDTO info = userService.getUserInfoById((Long) LongId);
        return List.of(UserTypeEnum.getUserTypeByCode(info.getUserType()));
    }
}
