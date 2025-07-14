import { Component } from '@angular/core';
import { Roomsearch } from "../roomsearch/roomsearch";
import { Roomresult } from "../roomresult/roomresult";

@Component({
  selector: 'app-home',
  imports: [Roomsearch, Roomresult],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  searchResults: any[] = [] // store the result of the searched room

  // handle the result comming from the roomsearch component
  handleSearchResult(results: any[]){
    this.searchResults = results
  }
}
