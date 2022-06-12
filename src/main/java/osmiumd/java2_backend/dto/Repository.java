package osmiumd.java2_backend.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class Repository {
    private int id;
    private String name;
    private int stars;
    private int forks;
    private String main_language;
    private Date create_time;
}
