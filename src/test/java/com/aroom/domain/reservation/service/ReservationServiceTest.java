package com.aroom.domain.reservation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.dto.response.ReservationResponse;
import com.aroom.domain.reservation.model.Reservation;
import com.aroom.domain.reservation.repository.ReservationRepository;
import com.aroom.domain.reservationRoom.model.ReservationRoom;
import com.aroom.domain.reservationRoom.repository.ReservationRoomRepository;
import com.aroom.domain.room.dto.request.RoomReservationRequest;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import com.aroom.domain.room.repository.RoomImageRepository;
import com.aroom.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ReservationRoomRepository reservationRoomRepository;
    @Mock
    private RoomImageRepository roomImageRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReservationService reservationService;


    private Accommodation testAccommodation;
    private RoomImage roomImage;
    private Room testRoom;
    private Member tester;

    @BeforeEach
    private void init(){
        testAccommodation = Accommodation.builder()
            .name("신라 호텔")
            .latitude(36.88475f)
            .longitude(127.150084f)
            .addressCode("15123")
            .likeCount(0)
            .phoneNumber("010-1234-1234")
            .roomList(null)
            .accommodationImageList(null)
            .build();

        roomImage = RoomImage.builder()
            .room(testRoom)
            .url("agsdas.com")
            .build();

        testRoom = Room.builder()
            .accommodation(testAccommodation)
            .type("패밀리")
            .price(100000)
            .capacity(2)
            .maxCapacity(4)
            .checkIn(LocalTime.of(19, 0))
            .checkOut(LocalTime.of(13, 0))
            .stock(1)
            .roomImageList(null)
            .build();

        tester = Member.builder()
            .name("김패캠")
            .email("qwgwf@naver.com")
            .password("56124WDA2@")
            .build();

    }

    @Nested
    @DisplayName("방 예약은 ")
    class SaveReservation{

        @Test
        @DisplayName("성공시 예약 정보를 반환한다.")
        void reservation_one_room_of_one_accommodation_success(){
            // given
            RoomReservationRequest roomRequest = RoomReservationRequest.builder()
                .roomId(1L)
                .startDate(LocalDate.of(2023, 12, 22))
                .endDate(LocalDate.of(2023, 12, 23))
                .build();

            ReservationRequest request = ReservationRequest.builder()
                .roomList(List.of(roomRequest))
                .personnel(2)
                .isFromCart(false)
                .agreement(true)
                .build();

            Reservation reservation = Reservation.builder()
                .member(Member.builder().build())
                .agreement(true)
                .build();

            ReservationRoom reservationRoom = ReservationRoom.builder()
                .room(testRoom)
                .reservation(reservation)
                .startDate(request.getRoomList().get(0).getStartDate())
                .endDate(request.getRoomList().get(0).getEndDate())
                .price(testRoom.getPrice())
                .personnel(request.getPersonnel())
                .build();

            given(memberRepository.findMemberByEmail(any())).willReturn(Optional.of(tester));
            given(roomRepository.findByIdWithLock(any())).willReturn(Optional.of(testRoom));
            given(reservationRoomRepository.save(any())).willReturn(reservationRoom);
            given(reservationRepository.save(any())).willReturn(reservation);
            given(roomImageRepository.findById(any())).willReturn(Optional.of(roomImage));

            // when
            ReservationResponse response = reservationService.reserveRoom(request, tester.getEmail());

            // then
            Assertions.assertThat(response).isNotNull();
            Assertions.assertThat(response.getRoomList().get(0).getType()).isEqualTo(testRoom.getType());
        }


        @Test
        @DisplayName("품절 상태인 방을 예약할 수 없다")
        void reservation_sold_out_room_fail(){
            // given
            RoomReservationRequest roomRequest = RoomReservationRequest.builder()
                .roomId(1L)
                .startDate(LocalDate.of(2023, 12, 22))
                .endDate(LocalDate.of(2023, 12, 23))
                .build();

            ReservationRequest request = ReservationRequest.builder()
                .roomList(List.of(roomRequest))
                .personnel(2)
                .agreement(true)
                .isFromCart(false)
                .build();

            given(memberRepository.findMemberByEmail(any())).willReturn(Optional.of(tester));
            given(roomRepository.findByIdWithLock(any())).willReturn(Optional.of(testRoom));
            given(reservationRoomRepository.getOverlappingReservationByDateRange(any(), any(), any())).willReturn(2);

            // when then
            Assertions.assertThatThrownBy(() -> {
                    reservationService.reserveRoom(request, tester.getName());
                }).isInstanceOf(RuntimeException.class)
                .hasMessage("방이 품절 되었습니다.");
        }
    }


}