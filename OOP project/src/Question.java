import java.io.Serializable;

abstract class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    String questionText;
    String correctAnswer;

    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}

class FillInTheBlanks extends Question {
    private static final long serialVersionUID = 2L;

    public FillInTheBlanks(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }
}

class MCQQuestion extends Question {
    private static final long serialVersionUID = 3L;

    public String[] options;

    public MCQQuestion(String questionText, String correctAnswer, String[] options) {
        super(questionText, correctAnswer);
        this.options = options;
    }
}
