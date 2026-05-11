package com.springbootinit.model.dto;

import com.springbootinit.model.domain.JbJobs;
import com.springbootinit.model.domain.UrMerchantProfiles;
import com.springbootinit.model.domain.WlWallets;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "兼职岗位详情DTO（包含商家档案和钱包信息）")
public class JobDetailDTO {

    @ApiModelProperty(value = "兼职岗位信息")
    private JbJobs job;

    @ApiModelProperty(value = "商家档案信息")
    private UrMerchantProfiles merchantProfile;

    @ApiModelProperty(value = "钱包信息")
    private WlWallets wallet;

    public JbJobs getJob() {
        return job;
    }

    public void setJob(JbJobs job) {
        this.job = job;
    }

    public UrMerchantProfiles getMerchantProfile() {
        return merchantProfile;
    }

    public void setMerchantProfile(UrMerchantProfiles merchantProfile) {
        this.merchantProfile = merchantProfile;
    }

    public WlWallets getWallet() {
        return wallet;
    }

    public void setWallet(WlWallets wallet) {
        this.wallet = wallet;
    }
}
