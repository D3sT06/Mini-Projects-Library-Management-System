package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.jpa.BookLoaningEntity;
import com.sahin.lms.infra.model.book.BookLoaning;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:23+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
@Primary
public class BookLoaningMapperImpl extends BookLoaningMapperDecorator implements BookLoaningMapper {

    @Autowired
    @Qualifier("delegate")
    private BookLoaningMapper delegate;

    @Override
    public List<BookLoaning> toModels(List<BookLoaningEntity> entities)  {
        return delegate.toModels( entities );
    }
}
