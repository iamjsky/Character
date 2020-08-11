package com.character.microblogapp.model;

import java.util.ArrayList;

public class MCharacter extends MBase {

    public class Answer {
        public String content;
        public String type;
    }

    public class Character {
        public String content;
        public ArrayList<Answer> answers;
    }

    public ArrayList<Character> character;
}
