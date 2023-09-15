package hello.springtx.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class OrderServiceTest {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@Test
	void order() throws NotEnoughMoneyException {
		//given
		Order order = new Order();
		order.setUsername("정상");
		//when
		orderService.order(order);
		//then

		Order findOrder = orderRepository.findById(order.getId()).get();
		assertThat(findOrder.getPayStatus()).isEqualTo("완료");
	}

	@Test
	void runtimeException() throws NotEnoughMoneyException {
		//given
		Order order = new Order();
		order.setUsername("예외");
		//when
		assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);
		//then

		Optional<Order> findOrder = orderRepository.findById(order.getId());
		assertThat(findOrder.isEmpty()).isTrue();
	}

	@Test
	void bzException(){
		//given
		Order order = new Order();
		order.setUsername("잔고부족");
		//when
		try {
			orderService.order(order);
		} catch (NotEnoughMoneyException e) {
			log.info("고객에게 잔고부족을 알리고 별도의 계좌로 입금하도록 안내");
		}

		//then
		Order findOrder = orderRepository.findById(order.getId()).get();
		assertThat(findOrder.getPayStatus()).isEqualTo("대기");
	}
}