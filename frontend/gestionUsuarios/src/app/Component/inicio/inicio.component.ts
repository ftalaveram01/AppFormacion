import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-inicio',
  imports: [CommonModule],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent implements OnInit {

  user: any;
  idUser!: number;
  idAdmin!: number;

  constructor(private userService: UserService, private router: ActivatedRoute) { }

  ngOnInit(): void {

    this.router.queryParams.subscribe(params => {
      this.userService.getUser(this.idUser, this.idUser).subscribe(data => {
        this.user = data;
      })
    })

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const tokenDecoded: any = jwtDecode(token)

    this.idAdmin = tokenDecoded.rol[0].rol.id;
    this.idUser = tokenDecoded.sub;
  }

  isAdmin(): boolean {
    return this.idAdmin == 0;
  }

  borrarCache(): void {
    localStorage.clear();
  }

}
