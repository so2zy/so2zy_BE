package com.aroom.domain.accommodation.dto;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.room.model.Room;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationListResponse {

    private Integer page;
    private Integer size;
    //private Integer totalPage;

    public void setPaginationInfo(Integer page, Integer size) {
        this.page = page;
        this.size = size;
        //this.totalPage = totalPage;
    }

    private List<InnerClass> body = new ArrayList<>();

    public void addBody(InnerClass innerClass) {
        this.body.add(innerClass);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class InnerClass {

        private Long id;

        private String name;

        private float latitude;

        private float longitude;

        private String addressCode;

        private int likeCount;

        private String phoneNumber;

        private String accommodationImageUrl;

        //객실의 최저가를 숙소 조회할때 대표 가격으로 출력합니다.
        private Integer price;

        public static InnerClass fromEntity(Accommodation accommodation) {
            String imageUrl = accommodation.getAccommodationImageList()
                .stream()
                .map(AccommodationImageList::fromEntity)
                .map(AccommodationImageList::getUrl)
                .findFirst()
                .orElse(null);
            int minimumPrice = accommodation.getRoomList().stream()
                .mapToInt(Room::getPrice)
                .min()
                .orElse(100000);

            return InnerClass.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .likeCount(accommodation.getLikeCount())
                .addressCode(accommodation.getAddressCode())
                .phoneNumber(accommodation.getPhoneNumber())
                .price(minimumPrice)
                .accommodationImageUrl(imageUrl)
                .build();
        }
    }
}
