package com.openplan.coupon.repository;

import com.openplan.coupon.dto.CouponFilterRequest;
import com.openplan.coupon.entity.PersonalCoupon;
import com.openplan.coupon.entity.QCouponBook;
import com.openplan.coupon.entity.QCouponInfo;
import com.openplan.coupon.entity.QPersonalCoupon;
import com.openplan.coupon.enums.CouponSortBy;
import com.openplan.coupon.enums.SortDirection;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonalCouponRepositoryCustomImpl implements PersonalCouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QPersonalCoupon personalCoupon = QPersonalCoupon.personalCoupon;
    private final QCouponBook couponBook = QCouponBook.couponBook;
    private final QCouponInfo couponInfo = QCouponInfo.couponInfo;

    @Override
    public List<PersonalCoupon> findByPersonIdWithFilters(String personId, CouponFilterRequest filter) {

        BooleanBuilder builder = generateCouponQueryBuilder(personId, filter);
        OrderSpecifier<?> orderBy = createOrderSpecifier(filter.getSortBy(), filter.getSortDirection());

        return queryFactory
            .selectFrom(personalCoupon)
            .join(couponBook).on(personalCoupon.couponCode.eq(couponBook.couponCode))
            .join(couponInfo).on(couponBook.couponInfo.couponInfoSeq.eq(couponInfo.couponInfoSeq))
            .where(builder)
            .orderBy(orderBy)
            .fetch();
    }

    private BooleanBuilder generateCouponQueryBuilder(String personId, CouponFilterRequest filter ) {
        if (filter == null) {
            return new BooleanBuilder();
        }

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(personalCoupon.personId.eq(personId));

        if (filter.getUsed() != null) {
            if (filter.getUsed()) {
                builder.and(personalCoupon.useAt.isNotNull());
            } else {
                builder.and(personalCoupon.useAt.isNull());
            }
        }

        if (filter.getCouponType() != null) {
            builder.and(couponInfo.couponType.eq(filter.getCouponType()));
        }

        if (filter.getPurposeType() != null) {
            builder.and(couponInfo.purposeType.eq(filter.getPurposeType()));
        }

        if (filter.getCouponNameContains() != null && !filter.getCouponNameContains().isEmpty()) {
            builder.and(couponInfo.couponName.contains(filter.getCouponNameContains()));
        }

        if (filter.getExpired() != null) {
            LocalDateTime now = LocalDateTime.now();
            if (filter.getExpired()) {
                builder.and(couponBook.expireAt.before(now));
            } else {
                builder.and(couponBook.expireAt.after(now));
            }
        }
        return builder;
    }

    private OrderSpecifier<?> createOrderSpecifier(CouponSortBy sortBy, SortDirection sortDirection) {

        boolean isAscending = sortDirection != SortDirection.DESC;

        if (sortBy == null) {
            return couponBook.expireAt.asc();
        }

        switch (sortBy) {
            case EXPIRE_AT:
                return isAscending ? couponBook.expireAt.asc() : couponBook.expireAt.desc();
            case COUPON_NAME:
                return isAscending ? couponInfo.couponName.asc() : couponInfo.couponName.desc();
            default:
                return couponBook.expireAt.asc();
        }
    }
}
