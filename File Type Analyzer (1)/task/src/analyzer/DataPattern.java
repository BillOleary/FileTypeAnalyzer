package analyzer;

/*
* The Data from the pattern db is objectified into the pattern below.
* The class is meant to be immutable as we do not anticipate any changes
* to the values down the track.
*
 */

class DataPattern {

    private String priority;
    private String pattern;
    private String fileType;

    public DataPattern(String priority, String pattern, String fileType) {
        this.priority = priority;
        this.pattern = pattern;
        this.fileType = fileType;
    }

    public String getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getFileType() {
        return fileType;
    }
}
