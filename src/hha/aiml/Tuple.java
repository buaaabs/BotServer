package hha.aiml;

import java.io.Serializable;

public abstract class Tuple implements Serializable {

    /**
     * gen by eclipse
     */
    private static final long serialVersionUID = -8719929625763890308L;

    public static <A, B> Tuple2<A, B> tuple(A a, B b) {
        return new Tuple2<A, B>(a, b);
    }

    public static <A, B, C, D, E> Tuple3<A, B, C> tuple(A a, B b, C c) {
        return new Tuple3<A, B, C>(a, b, c);
    }

    public static final class Tuple2<A, B> extends Tuple implements Serializable {

        /**
         * gen by eclipse
         */
        private static final long serialVersionUID = 7263645006696591635L;
        private A a;
        private B b;

        private Tuple2(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A _1() {
            return this.a;
        }

        public B _2() {
            return this.b;
        }
    }

    public static final class Tuple3<A, B, C> extends Tuple implements Serializable {

        /**
         * gen by eclipse
         */
        private static final long serialVersionUID = 482545776907024160L;

        private A a;
        private B b;
        private C c;

        private Tuple3(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public A _1() {
            return this.a;
        }

        public B _2() {
            return this.b;
        }

        public C _3() {
            return this.c;
        }
    }
}
