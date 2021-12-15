package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.shortest.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.shortest.ShortestPath;
import nextstep.subway.path.domain.shortest.ShortestPathFinder;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private final ShortestPathFinder shortestPathFinder;

	private PathFinder(List<Line> lines) {
		validate(lines);

		this.shortestPathFinder = DijkstraShortestPathFinder.of();
		lines.stream()
			.flatMap(line -> line.getSections().stream())
			.forEach(this::addSection);
	}

	private void validate(List<Line> lines) {
		if (null == lines) {
			throw new IllegalArgumentException("노선 목록이 있어야 합니다.");
		}
	}

	private void addSection(Section section) {
		shortestPathFinder.addEdge(
			section.getUpStation(),
			section.getDownStation(),
			section.getDistance()
		);
	}

	public ShortestPath findShortest(Station source, Station target) {
		validateToFindShortest(source, target);
		try {
			return shortestPathFinder.find(source, target);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("노선에 포함되지 않은 역입니다.");
		}
	}

	private void validateToFindShortest(Station source, Station target) {
		if (Objects.equals(source, target)) {
			throw new IllegalArgumentException("경로의 출발역과 도착역은 서로 달라야 합니다.");
		}
	}

	public static PathFinder of(List<Line> lines) {
		return new PathFinder(lines);
	}
}
