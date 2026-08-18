// MediConnect API Module
const API_BASE = 'http://localhost:8080';

class ApiClient {
    constructor() {
        this.baseUrl = API_BASE;
    }

    getToken() {
        return sessionStorage.getItem('jwt_token');
    }

    setToken(token) {
        sessionStorage.setItem('jwt_token', token);
    }

    clearToken() {
        sessionStorage.removeItem('jwt_token');
        sessionStorage.removeItem('user_role');
        sessionStorage.removeItem('user_id');
    }

    getHeaders(includeAuth = true) {
        const headers = { 'Content-Type': 'application/json' };
        if (includeAuth && this.getToken()) {
            headers['Authorization'] = `Bearer ${this.getToken()}`;
        }
        return headers;
    }

    async request(method, path, body = null, includeAuth = true) {
        const url = `${this.baseUrl}${path}`;
        const options = {
            method,
            headers: this.getHeaders(includeAuth),
        };
        if (body) options.body = JSON.stringify(body);

        try {
            const response = await fetch(url, options);
            const data = response.headers.get('content-type')?.includes('application/json')
                ? await response.json()
                : null;

            if (response.status === 401) {
                this.clearToken();
                window.location.href = 'login.html';
                return;
            }

            if (!response.ok) {
                throw { status: response.status, data };
            }

            return data;
        } catch (error) {
            if (error.status) throw error;
            throw { status: 500, data: { message: 'Network error. Please try again.' } };
        }
    }

    // Auth
    register(data) { return this.request('POST', '/api/auth/register', data, false); }
    login(data) { return this.request('POST', '/api/auth/login', data, false); }
    getProfile(userId) { return this.request('GET', `/api/auth/profile/${userId}`); }

    // Patients
    createPatient(data) { return this.request('POST', '/api/patients', data); }
    getPatient(id) { return this.request('GET', `/api/patients/${id}`); }
    updatePatient(id, data) { return this.request('PUT', `/api/patients/${id}`, data); }
    deletePatient(id) { return this.request('DELETE', `/api/patients/${id}`); }
    getPatientByUserId(userId) { return this.request('GET', `/api/patients/user/${userId}`); }

    // Doctors
    createDoctor(data) { return this.request('POST', '/api/doctors', data); }
    getAllDoctors() { return this.request('GET', '/api/doctors'); }
    getDoctor(id) { return this.request('GET', `/api/doctors/${id}`); }
    updateDoctor(id, data) { return this.request('PUT', `/api/doctors/${id}`, data); }
    deleteDoctor(id) { return this.request('DELETE', `/api/doctors/${id}`); }
    getDoctorByUserId(userId) { return this.request('GET', `/api/doctors/user/${userId}`); }
    searchDoctors(keyword) { return this.request('GET', `/api/doctors/search?keyword=${keyword}`); }
    getDoctorsBySpecialization(spec) { return this.request('GET', `/api/doctors/specialization/${spec}`); }
    getAvailableDoctors() { return this.request('GET', '/api/doctors/available'); }

    // Appointments
    bookAppointment(data) { return this.request('POST', '/api/appointments', data); }
    getAppointment(id) { return this.request('GET', `/api/appointments/${id}`); }
    getAppointmentsByPatient(id) { return this.request('GET', `/api/appointments/patient/${id}`); }
    getAppointmentsByDoctor(id) { return this.request('GET', `/api/appointments/doctor/${id}`); }
    confirmAppointment(id) { return this.request('PUT', `/api/appointments/${id}/confirm`); }
    cancelAppointment(id) { return this.request('PUT', `/api/appointments/${id}/cancel`); }
    completeAppointment(id) { return this.request('PUT', `/api/appointments/${id}/complete`); }
    rescheduleAppointment(id, data) { return this.request('PUT', `/api/appointments/${id}/reschedule`, data); }

    // Medical Records
    createMedicalRecord(data) { return this.request('POST', '/api/medical-records', data); }
    getMedicalRecord(id) { return this.request('GET', `/api/medical-records/${id}`); }
    getRecordsByPatient(id) { return this.request('GET', `/api/medical-records/patient/${id}`); }
    getRecordsByDoctor(id) { return this.request('GET', `/api/medical-records/doctor/${id}`); }
    updateMedicalRecord(id, data) { return this.request('PUT', `/api/medical-records/${id}`, data); }
    deleteMedicalRecord(id) { return this.request('DELETE', `/api/medical-records/${id}`); }

    // Notifications
    getNotifications(userId) { return this.request('GET', `/api/notifications/user/${userId}`); }
    getUnreadCount(userId) { return this.request('GET', `/api/notifications/user/${userId}/count`); }
    markNotificationRead(id) { return this.request('PUT', `/api/notifications/${id}/read`); }

    // Admin
    adminGetUsers(page = 0, size = 10) { return this.request('GET', `/api/admin/users?page=${page}&size=${size}`); }
    adminGetUser(id) { return this.request('GET', `/api/admin/users/${id}`); }
    adminToggleUser(id) { return this.request('PUT', `/api/admin/users/${id}/enable`); }
    adminDeleteUser(id) { return this.request('DELETE', `/api/admin/users/${id}`); }
    adminGetStatistics() { return this.request('GET', '/api/admin/statistics'); }
}

const api = new ApiClient();
