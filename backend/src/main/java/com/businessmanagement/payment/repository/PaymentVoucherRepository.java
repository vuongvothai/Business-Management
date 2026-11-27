package com.businessmanagement.payment.repository;

import com.businessmanagement.payment.entity.PaymentVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher, Long> {
    List<PaymentVoucher> findByVoucherTypeOrderByVoucherDateDesc(String voucherType);
    List<PaymentVoucher> findByPartyTypeAndPartyIdOrderByVoucherDateDesc(String partyType, Long partyId);
}