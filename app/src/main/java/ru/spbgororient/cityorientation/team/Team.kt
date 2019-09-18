package ru.spbgororient.cityorientation.team

import android.content.SharedPreferences

class Team {
    private val spLogin = "login"
    private val spPassword = "password"

    var login = "Login"
    var password = "Password"
    var teamName = "Team Name"

    /**
     * Загружает данные из [settings].
     *
     * Считывает [login], [password] из [settings], если они существуют и возвращает true. Иначе false.
     *
     * @param[settings] экземпляр SharedPreferences.
     * @return возвращает true, если [login], [password] есть в базе. Иначе false.
     */
    fun load(settings: SharedPreferences): Boolean {
        if (settings.contains(spLogin) && settings.contains(spPassword)) {
            login = settings.getString(spLogin, "")!!
            password = settings.getString(spPassword, "")!!
            return true
        }
        return false
    }

    /**
     * Сохраняет [login] и [password] команды.
     *
     * @param[login] логин команды.
     * @param[password] пароль команды.
     * @param[settings] экземпляр SharedPreferences.
     */
    fun save(login: String, password: String, settings: SharedPreferences) {
        this.login = login
        this.password = password
        val editor = settings.edit()
        editor.putString(spLogin, login)
        editor.putString(spPassword, password)
        editor.apply()
    }

    /**
     * Удаляет [login] и [password] команды.
     *
     * @param[settings] экземпляр SharedPreferences.
     */
    fun reset(settings: SharedPreferences) {
        login = ""
        password = ""
        teamName = ""
        val editor = settings.edit()
        editor.remove(spLogin)
        editor.remove(spPassword)
        editor.apply()
    }

    /**
     * Переименовывает название команды.
     *
     * @param[teamName] новое название команды.
     */
    fun rename(teamName: String) {
        this.teamName = teamName
    }
}