package com.kefas.EWallet_aop.pojo.user.response;

import com.kefas.EWallet_aop.entity.User;
import com.kefas.EWallet_aop.entity.Wallet;
import com.kefas.EWallet_aop.pojo.wallet.response.WalletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponse {
    private RegisterResponse user;
    private WalletResponse wallet;


    public static UserResponse mapFromData(User userData, Wallet userWallet) {
        RegisterResponse user = RegisterResponse.mapFromUser(userData);
        WalletResponse wallet = WalletResponse.mapFromWallet(userWallet);

        return UserResponse.builder()
                .user(user)
                .wallet(wallet)
                .build();
    }

    public static List<UserResponse> mapFromData(List<User> userList, List<Wallet> walletList) {
        List<RegisterResponse> users = RegisterResponse.mapFromUser(userList);
        List<WalletResponse> wallets = WalletResponse.mapFromWallet(walletList);

        List<UserResponse> userResponses = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            RegisterResponse user = users.get(i);

            WalletResponse wallet;
            try {
                wallet = wallets.get(i);
            } catch (Exception e) {
                wallet = null;
            }

            var userResponse = UserResponse.builder()
                    .user(user)
                    .wallet(wallet)
                    .build();

            userResponses.add(userResponse);
        }

        return userResponses;
    }
}
