package hello.springtx.propagation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	LogRepository logRepository;

	/**
	 * memberService : @Transactional off
	 * memberRepository : @Transactional on
	 * logRepository : @Transactional on
	 */
	@Test
	void outerTxOff_success() {
		//given
		String username = "outerTxOff_success";
		//when
		memberService.joinV1(username);
		//then
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}

	/**
	 * memberService : @Transactional off
	 * memberRepository : @Transactional on
	 * logRepository : @Transactional on Exception
	 */
	@Test
	void outerTxOff_fail() {
		//given
		String username = "로그예외_outerTxOff_fail";
		//when
		assertThatThrownBy(() -> memberService.joinV1((username))).isInstanceOf(
			RuntimeException.class);
		//then
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isEmpty());
	}

	/**
	 * memberService : @Transactional on
	 * memberRepository : @Transactional off
	 * logRepository : @Transactional off
	 */
	@Test
	void singleTx() {
		//given
		String username = "singleTx";
		//when
		memberService.joinV1(username);
		//then
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}

	/**
	 * memberService : @Transactional on
	 * memberRepository : @Transactional on
	 * logRepository : @Transactional on
	 */
	@Test
	void outerTxOn_success() {
		//given
		String username = "outerTxOn_success";
		//when
		memberService.joinV1(username);
		//then
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}

	/**
	 * memberService : @Transactional off
	 * memberRepository : @Transactional on
	 * logRepository : @Transactional on Exception
	 */
	@Test
	void outerTxOn_fail() {
		//given
		String username = "로그예외_outerTxOn_fail";
		//when
		assertThatThrownBy(() -> memberService.joinV1((username))).isInstanceOf(
			RuntimeException.class);
		//then
		assertTrue(memberRepository.find(username).isEmpty());
		assertTrue(logRepository.find(username).isEmpty());
	}

	/**
	 * memberService : @Transactional on
	 * memberRepository : @Transactional on
	 * logRepository : @Transactional on Exception
	 */
	@Test
	void recoverException_fail() {
		//given
		String username = "로그예외_recoverException_fail";
		//when
		assertThatThrownBy(() -> memberService.joinV2((username))).isInstanceOf(
			UnexpectedRollbackException.class);
		//then
		assertTrue(memberRepository.find(username).isEmpty());
		assertTrue(logRepository.find(username).isEmpty());
	}

	/**
	 * memberService : @Transactional on
	 * memberRepository : @Transactional on
	 * logRepository : @Transactional on(requires_new) Exception
	 */
	@Test
	void recoverException_success() {
		//given
		String username = "로그예외_recoverException_success";
		//when
		memberService.joinV2(username);
		//then
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isEmpty());
	}
}