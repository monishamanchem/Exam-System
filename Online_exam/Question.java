package Online_exam;

public class Question {
    private int id;
    private String questionText;
    private String[] options;
    private int correctOptionIndex;

    public Question(int id, String questionText, String[] options, int correctOptionIndex) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    // Overload constructor for questions without an ID (used for inserts)
    public Question(String questionText, String[] options, int correctOptionIndex) {
        this(-1, questionText, options, correctOptionIndex);
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }
}
