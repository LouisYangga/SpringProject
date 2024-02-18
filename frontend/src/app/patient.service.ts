import { Injectable } from "@angular/core";
import { Patient } from "./patient";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class PatientService {
    private apiServerUrl = 'http://localhost:8080/api/patient';

    constructor(private http: HttpClient){ }

    public getPatients(): Observable<Patient[]>{
        return this.http.get<Patient[]>(`${this.apiServerUrl}/patients`);  
    }
    public findPatient(patientId: number): Observable<Patient> {
        const url = `${this.apiServerUrl}/${patientId}`; 
        return this.http.get<Patient>(url);  
    }
    public addPatient(patient: Patient): Observable<string>{
        return this.http.post(`${this.apiServerUrl}`, patient,{responseType:'text'});  
    }
    public updatePatient(patient: Patient): Observable<void> {
        const url = `${this.apiServerUrl}/${patient.id}`; 
        return this.http.put<void>(url, patient);  
    }
    public deletePatient(patientId: number): Observable<void> {
        const url = `${this.apiServerUrl}/${patientId}`;
        return this.http.delete<void>(url);  
    }
}