package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        return LineResponse.of(lineRepository.save(createLineOf(request)));
    }

    public List<LineResponse> findLines() {
        return findAllLines()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("`Line` " + id + " 는 존재하지 않습니다."));
    }


    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLineById(id));
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        findLineById(id).update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        List<Station> stations = stationService.findByIdIn(Arrays.asList(request.getUpStationId(), request.getDownStationId()));
        Station upStation = extractStation(stations, request.getUpStationId());
        Station downStation = extractStation(stations, request.getDownStationId());
        findLineById(lineId).addSection(upStation, downStation, request.getDistance());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Station station = stationService.findById(stationId);
        findLineById(lineId).deleteStation(station);
    }

    private Line createLineOf(LineRequest request) {
        List<Station> stations = stationService.findByIdIn(Arrays.asList(request.getUpStationId(), request.getDownStationId()));
        Station upStation = extractStation(stations, request.getUpStationId());
        Station downStation = extractStation(stations, request.getDownStationId());
        return new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance(), request.getAdditionalFare());
    }

    private Station extractStation(List<Station> stations, Long stationId) {
        return stations.stream()
                .filter(s -> s.isSameId(stationId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Station id:" + stationId + " 존재하지않습니다."));
    }
}
