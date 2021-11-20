package abdulr.mian.betterreads.app.author;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;


@Table(value = "author_by_id")
@Getter
@Setter
public class Author {

    @Id @PrimaryKeyColumn(name = "author_id", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private String id;

    @Column("author_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("author_privateName")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String privateName;

}
