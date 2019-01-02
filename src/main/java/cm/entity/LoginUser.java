package cm.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class LoginUser implements UserDetails {
    private Long userId;
    private String userAccount;
    private String passWord;
    private Byte isActive;
    private String userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        System.out.println(this.userRole);
        String aurhority=this.userRole;
        GrantedAuthority au = new SimpleGrantedAuthority(aurhority);
        list.add(au);
        return list;
    }
    @Override
    public String getPassword() {
        return passWord;
    }
    @Override
    public String getUsername() {
        return userAccount;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
