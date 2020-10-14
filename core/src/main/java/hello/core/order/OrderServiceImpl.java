package hello.core.order;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    //private final MemberRepository memberRepository = new MemoryMemberRepository();

    // 'FixDiscountPolicy'를 'RateDiscountPolicy'로 변경하는 순간 'OrderServiceImpl'의 소스코드도 함께 변경해야 한다. => OCP 위반
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    // private DiscountPolicy discountPolicy;

    // 설계 변경으로 OrderServiceImpl은 FixDiscountPolicy를 의존하지 않는다. 단지 DiscountPolicy 인터페이스만 의존한다.
    // OrderServiceImpl 입장에서 생성자를 통해 어떤 구현 객체가 들어올지(주입될지)는 알 수 없다.
    // OrderServiceImpl의 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부(AppConfig)에서 결정한다.
    // OrderServiceImpl은 이제부터 실행에만 집중하면 된다.
    // OrderServiceImpl에는 MemoryMemberRepository, FixDiscountPolicy 객체의 의존관계가 주입된다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

     @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice); // member 넘겨도 되고, grade만 넘겨도 되고 회사 정책에 따라 다름

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}