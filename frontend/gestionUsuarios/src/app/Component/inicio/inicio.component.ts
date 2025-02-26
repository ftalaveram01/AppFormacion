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

    this.router.queryParams.subscribe(params => {
      this.idUser = +params['id']
      this.idAdmin = +params['idAdmin']
      localStorage.setItem('idAdmin', Number(this.idAdmin).toString());
      this.userService.getUser(this.idUser, this.idUser).subscribe(data=>{
        this.user = data;
      })
    })
  }

  isAdmin(): boolean {
    return this.idAdmin == 0;
  }

  borrarCache(): void {
    localStorage.clear();
  }

}
