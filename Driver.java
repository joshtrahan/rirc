/*
    RIRC - My personal IRC library
    Copyright (C) 2017  Joshua Trahan

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import com.robut.rirc.Message;

public class Driver {
    public static void main(String[] args) {
        try {
            tryMsg(":vypur!vypur@vypur.tmi.twitch.tv PRIVMSG #twitchpresents :ATTACK ME AKIZA moon2L");
            tryMsg("PING tmi.twitch.tv");
        }
        catch (Exception e){
            System.err.printf("Exception: %s%n", e);
        }

        return;
    }

    public static void tryMsg(String str) throws Exception{
        Message msg = new Message(str);
        System.out.printf("Oper: %s%n", msg.getOp());
        System.out.printf("User: %s%n", msg.getUser());
        System.out.printf("Chan: %s%n", msg.getChannel());
        System.out.printf("Mesg: %s%n%n", msg.getMessage());

        if (msg.getOp().equalsIgnoreCase("PING")) {
            System.out.println("PONG " + msg.getMessage());
        }
    }
}
