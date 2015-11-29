package cs555.project.util;

/**
 * @author Thilina Buddhika
 */
public class Constants {

    public static final int PICO_TO_MILLI = 1000000000;
    public static final String INPUT_FILE = "input-file";
    public static final int PLAYER_PERF_SLIDING_WINDOW_LEN = 5 * 1000;

    public class Streams {
        public static final String PLAYER_POSITIONS = "player-positions";
        public static final String PLAYER_BALL_POSITIONS = "player-ball-positions";
        public static final String SHOTS_ON_GALL = "shots-on-gall";
    }

    public class Fields {
        public static final String RAW_SID = "sid";
        public static final String RAW_TIMESTAMP = "ts";
        public static final String RAW_LOC_X = "loc-x";
        public static final String RAW_LOC_Y = "loc-y";
        public static final String RAW_LOC_Z = "loc-z";
        public static final String RAW_VELOCITY = "velocity";
        public static final String RAW_ACCELERATION = "acceleration";
        public static final String RAW_VEL_X = "vel-x";
        public static final String RAW_VEL_Y = "vel-y";
        public static final String RAW_VEL_Z = "vel-z";
        public static final String RAW_ACC_X = "acc-x";
        public static final String RAW_ACC_Y = "acc-y";
        public static final String RAW_ACC_Z = "acc-z";
        // meta-data
        public static final String META_NAME = "meta-name";
        public static final String META_TEAM = "meta-team";
        public static final String META_LEG = "meta-leg";
    }
}
