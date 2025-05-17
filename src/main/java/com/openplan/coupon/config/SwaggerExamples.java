package com.openplan.coupon.config;

public final class SwaggerExamples {

    private SwaggerExamples() {
        // Prevent instantiation
    }

    public static final String CouponInfoCreateRequest = """
        {
          "couponType": "ONCE",
          "couponName": "신규 가입자 10% 할인 쿠폰",
          "targetType": "B2C",
          "couponPublishType": "POLY",
          "purposeType": "RATE",
          "purposeValue": "10",
          "couponBadgeType": "NEW",
          "couponImageUrl": "string",
          "pressCount": 100,
          "useCount": 0,
          "limitCount": 1,
          "isAble": true,
          "isDuplicate": false,
          "couponStartAt": "2025-05-17T13:37:55.761Z",
          "couponEndAt": "2025-06-17T13:37:55.761Z"
        }
        """;

    public static final String CouponInfoUpdateRequest = """
        {
          "purposeValue": "20"
        }
        """;

    public static final String CouponConditionCreateRequest = """
        {
          "conditionType": "MAX_DISCOUNT",
          "mainValue": "1000",
          "subValue": "-",
          "conditionDesc": "최대할인 금액 1000원"
        }
        """;

    public static final String CouponConditionUpdateRequest = """
        {
          "mainValue": "2000"
        }
        """;

    public static final String CouponBookCreateRequest = """
        {
          "couponInfoSeq": 3,
          "adminId": "admin123"
        }
        """;

    public static final String CouponFilterRequest = """
        {
          "available": true
        }
        """;
}
