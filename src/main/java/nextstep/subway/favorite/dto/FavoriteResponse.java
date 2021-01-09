package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
	private Long id;
	private StationResponse source;
	private StationResponse target;

	public FavoriteResponse() {
	}

	public static FavoriteResponse of(Favorite favorite) {
		StationResponse source = StationResponse.of(favorite.getSource());
		StationResponse target = StationResponse.of(favorite.getTarget());
		return new FavoriteResponse(favorite.getId(), source, target);
	}

	private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public Long getId() {
		return id;
	}

	public StationResponse getSource() {
		return source;
	}

	public StationResponse getTarget() {
		return target;
	}
}