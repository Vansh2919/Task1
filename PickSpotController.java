@RestController
public class PickSpotController {

    @PostMapping("/pickSpot")
    public ResponseEntity<Object> pickSpot(@RequestBody PickSpotRequest request) {
        Container c = request.container;
        List<YardSlot> yardMap = request.yardMap;

        YardSlot bestSlot = null;
        int bestScore = Integer.MAX_VALUE;

        for (YardSlot s : yardMap) {
            int score = SpotScorer.computeScore(c, s);
            if (score < bestScore) {
                bestScore = score;
                bestSlot = s;
            }
        }

        if (bestScore >= SpotScorer.INVALID || bestSlot == null) {
            return ResponseEntity.ok(Map.of("error", "no suitable slot"));
        }

        return ResponseEntity.ok(Map.of(
            "containerId", c.id,
            "targetX", bestSlot.x,
            "targetY", bestSlot.y
        ));
    }
}
