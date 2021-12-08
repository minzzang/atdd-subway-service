package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteRequest {
    private final Long source;
    private final Long target;

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }


}
