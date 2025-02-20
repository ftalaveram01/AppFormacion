import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-inicio',
  imports: [CommonModule],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent implements OnInit{

  user: any;
  idUser!: number;
  idAdmin!: number;

  constructor(private userService: UserService, private router: ActivatedRoute){ }

  ngOnInit(): void {
    //TODO

    this.router.queryParams.subscribe(params => {
      this.idUser = +params['id']
      this.idAdmin = +params['idAdmin']
      this.userService.getUser(this.idUser, this.idAdmin).subscribe(data=>{
        this.user = data;
      })
    })
  }

  isAdmin(): boolean {
    return this.idAdmin == 0;
  }



  
}
