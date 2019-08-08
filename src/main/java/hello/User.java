package hello;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User
{
    @NotEmpty(message = "Cannot be empty")
    @NotNull(message = "Cannot be null")
    private String emailSubject;

    @NotEmpty(message = "Cannot be empty")
    @NotNull(message = "Cannot be null")
    @NotBlank(message = "Cannot be blank")
    private String emailTo;
    @NotEmpty(message = "Cannot be empty")
    private String messageContent;

    public User()
    {

    }

    public User(String emailSubject, String emailTo, String messageContent)
    {
        this.emailSubject = emailSubject;
        this.emailTo = emailTo;
        this.messageContent=messageContent;
    }

    public String getEmailSubject()
    {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject)
    {
        this.emailSubject = emailSubject;
    }

    public String getEmailTo()
    {
        return emailTo;
    }

    public void setEmailTo(String emailTo)
    {
        this.emailTo = emailTo;
    }

    public String getMessageContent()
    {
        return messageContent;
    }

    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }
}
