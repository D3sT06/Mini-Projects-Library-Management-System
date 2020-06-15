package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.MemberEntity;
import com.sahin.library_management.infra.model.account.Member;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Autowired
    private LibraryCardMapper libraryCardMapper;

    @Override
    public Member toModel(MemberEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Member member = new Member();

        member.setId( entity.getId() );
        member.setLibraryCard( libraryCardMapper.toModel( entity.getLibraryCard() ) );
        member.setName( entity.getName() );
        member.setSurname( entity.getSurname() );
        member.setEmail( entity.getEmail() );
        member.setPhone( entity.getPhone() );

        return member;
    }

    @Override
    public MemberEntity toEntity(Member model) {
        if ( model == null ) {
            return null;
        }

        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setId( model.getId() );
        memberEntity.setLibraryCard( libraryCardMapper.toEntity( model.getLibraryCard() ) );
        memberEntity.setName( model.getName() );
        memberEntity.setSurname( model.getSurname() );
        memberEntity.setEmail( model.getEmail() );
        memberEntity.setPhone( model.getPhone() );

        return memberEntity;
    }
}
