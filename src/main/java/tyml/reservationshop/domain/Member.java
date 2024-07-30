package tyml.reservationshop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import tyml.reservationshop.domain.dto.MemberForm;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Setter
    private String pw;

    @Column(unique = true)
    private String email;

    private String phoneNumber;



    @Embedded
    private Address address;


    @OneToMany(mappedBy = "member")
    private List<Reserv> Reservs = new ArrayList<Reserv>();

    protected Member() {}
    public Member(MemberForm form)
    {
        this.name = form.getName();
        this.pw = form.getPw();
        this.phoneNumber = form.getPhoneNumber();
        this.email = form.getEmail();
        this.address = new Address(form.getPostcodes(), form.getAddress(), form.getDetailAddress());
    }

    public void UpdateFromMemberForm(MemberForm form)
    {
        this.name = form.getName();
        this.pw = form.getPw();
        this.phoneNumber = form.getPhoneNumber();
        this.email = form.getEmail();
        this.address = new Address(form.getPostcodes(), form.getAddress(), form.getDetailAddress());
    }

}
