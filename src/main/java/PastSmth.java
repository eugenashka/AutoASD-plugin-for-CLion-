import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class PastSmth extends AnAction {

    private static @NotNull Map<String, String> getOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("One-ordered list", """
                struct Node {
                      int value;
                      Node *next;
                  };
                
                  struct one_ordered_list {
                      Node *first;
                      Node *last;
                
                      one_ordered_list() : first(nullptr), last(nullptr) {}
                
                      bool empty() {
                          return first == nullptr;
                      }
                
                      void push_back(int _val) {
                          Node *p = new Node(_val);
                          if (empty()) {
                              first = p;
                              last = p;
                              return;
                          }
                          last->next = p;
                          last = p;
                      }
                
                      void print() {
                          if (empty()) return;
                          Node *p = first;
                          while (p) {
                              cout << p->value << " ";
                              p = p->next;
                          }
                          cout << endl;
                      }
                
                      Node *find(int _val) {
                          Node *p = first;
                          while (p && p->value != _val) p = p->next;
                          return (p && p->value == _val) ? p : nullptr;
                      }
                
                      void pop_front() {
                          if (empty()) return;
                          Node *p = first;
                          first = p->next;
                          delete p;
                      }
                
                      void pop_back() {
                          if (empty()) return;
                          if (first == last) {
                              pop_front();
                              return;
                          }
                          Node *p = first;
                          while (p->next != last) p = p->next;
                          p->next = nullptr;
                          delete last;
                          last = p;
                      }
                
                      void pop_key(int _val) {
                          if (empty()) return;
                          if (first->value == _val) {
                              pop_front();
                              return;
                          } else if (last->value == _val) {
                              pop_back();
                              return;
                          }
                          Node *slow = first;
                          Node *fast = first->next;
                          while (fast && fast->value != _val) {
                              fast = fast->next;
                              slow = slow->next;
                          }
                          if (!fast) {
                              cout << "This element does not exist" << endl;
                              return;
                          }
                          slow->next = fast->next;
                          delete fast;
                      }
                
                      Node *operator[](const int ind) {
                          if (empty()) return nullptr;
                          Node *p = first;
                          for (int i = 0; i < ind; i++) {
                              p = p->next;
                              if (!p) return nullptr;
                          }
                          return p;
                      }
                  };
                """
        );
        options.put("Two-ordered list", """
                struct Node {
                      int value;
                      struct Node *next;
                      struct Node *prev;
                  };
                
                  struct two_ordered_list {
                      Node *first;
                      Node *last;
                
                      two_ordered_list() : first(nullptr), last(nullptr) {}
                
                      bool empty() {
                          return first == nullptr;
                      }
                
                      void push_back(int val) {
                          Node *now = new Node;
                          now->value = val;
                          now->next = nullptr;
                          now->prev = last;
                          if (last != nullptr) {
                              last->next = now;
                          }
                          last = now;
                          if (empty()) {
                              first = last;
                          }
                      }
                
                      void push_front(int val) {
                          Node *now = new Node;
                          now->value = val;
                          now->next = first;
                          now->prev = nullptr;
                          if (first != nullptr) {
                              first->prev = now;
                          }
                          first = now;
                          if (first->next == nullptr) {
                              last = first;
                          }
                      }
                
                      int getlast() {
                          return last->value;
                      }
                
                      int getfirst() {
                          return first->value;
                      }
                
                      void pop_back() {
                          if (empty()) return;
                          Node *now = last;
                          last = last->prev;
                          if (last != nullptr) {
                              last->next = nullptr;
                          } else {
                              first = nullptr;
                          }
                          delete now;
                      }
                
                      void pop_front() {
                          if (empty()) return;
                          Node *now = first;
                          first = first->next;
                          if (first != nullptr) {
                              first->prev = nullptr;
                          } else {
                              first = nullptr;
                          }
                      }
                
                      Node *operator[](int pos) {
                          Node *now = first;
                          for (int i = 0; i < pos; i++) {
                              now = now->next;
                          }
                          return now;
                      }
                
                      void print() {
                          if (empty()) return;
                          Node *p = first;
                          while (p) {
                              cout << p->value << " ";
                              p = p->next;
                          }
                          cout << endl;
                      }
                  };
                """);
        options.put("Geometry functions", """
                const long double EPS = 0.000000001;
                const long double pi = atan(1) * 4;
                
                struct point {
                    long double x, y;
                
                    point() {
                        x = 0;
                        y = 0;
                    }
                
                    point(long double x_, long double y_) {
                        x = x_;
                        y = y_;
                    }
                
                    point(point a, point b) {
                        x = b.x - a.x;
                        y = b.y - a.y;
                    }
                
                    bool operator ==(point other) const {
                        return x == other.x && y == other.y;
                    }
                
                    point operator +(point other) const {
                        point res;
                        res.x = x + other.x;
                        res.y = y + other.y;
                        return res;
                    }
                
                    point operator -(point other) const {
                        point res;
                        res.x = x - other.x;
                        res.y = y - other.y;
                        return res;
                    }
                
                    point operator *(long double d) const {
                        point res;
                        res.x = x * d;
                        res.y = y * d;
                        return res;
                    }
                
                    long double operator *(point other) const {
                        long double res = x * other.y - other.x * y;
                        return res;
                    }
                
                    long double operator ^(point other) const {
                        long double res = x * other.x + y * other.y;
                        return res;
                    }
                
                    long double sqLen() {
                        return x * x + y * y;
                    }
                
                    long double len() {
                        return sqrt(sqLen());
                    }
                };
                \s
                istream& operator >>(istream& in, point& pnt) {
                    in >> pnt.x >> pnt.y;
                    return in;
                }
                \s
                ostream& operator <<(ostream& out, point& pnt) {
                    out << pnt.x << " " << pnt.y;
                    return out;
                }
                \s
                long double distPointToPoint(point a, point b) {
                    point ab = point(a, b);
                    return ab.len();
                }
                \s
                long double getAngle(point o, point a, point b) {
                    point oa = point(o, a);
                    point ob = point(o, b);
                
                    long double cos = (oa ^ ob) / (oa.len() * ob.len());
                
                    if (cos > 1)
                        cos = 1;
                    if (cos < -1)
                        cos = -1;
                
                    return acos(cos) * 180 / pi;
                }
                \s
                struct Line {
                    long double a, b, c;
                
                    Line() {
                        a = 0;
                        b = 0;
                        c = 0;
                    }
                
                    Line(long double a_, long double b_, long double c_) {
                        a = a_;
                        b = b_;
                        c = c_;
                    }
                
                    Line(point u, point v) {
                        a = v.y - u.y;
                        b = u.x - v.x;
                        c = -(u * v);
                    }
                
                    long double distToPoint(point pnt) {
                        long double x = pnt.x;
                        long double y = pnt.y;
                
                        long double res = 0;
                
                        if (a == 0 && b == 0)
                            res = c - x;
                        else
                            res = (a * x + b * y + c) / sqrt(a * a + b * b);
                        res = abs(res);
                
                        return res;
                    }
                
                    pair<int, point> crossWithLine(Line l) {
                        long double a2 = l.a;
                        long double b2 = l.b;
                        long double c2 = l.c;
                
                        int status = 0;
                        point res = point();
                
                        if (a * b2 == a2 * b) {
                            if (a * c2 == a2 * c && b * c2 == b2 * c)
                                status = 2;
                            else
                                status = 0;
                        }
                        else {
                            status = 1;
                            long double x = (b * c2 - b2 * c) / (a * b2 - a2 * b);
                            long double y = (a2 * c - a * c2) / (a * b2 - a2 * b);
                            res = point(x, y);
                        }
                
                        return { status, res };
                    }
                };
                \s
                istream& operator >>(istream& in, Line& l) {
                    in >> l.a >> l.b >> l.c;
                    return in;
                }
                \s
                struct Cycle {
                    point o;
                    long double r;
                
                    Cycle() {
                        o = point();
                        r = 0;
                    }
                
                    Cycle(point o_, long double r_) {
                        o = o_;
                        r = r_;
                    }
                
                    Cycle(point p1, point p2, point p3) {
                        long double a = distPointToPoint(p1, p2);
                        long double b = distPointToPoint(p2, p3);
                        long double c = distPointToPoint(p3, p1);
                
                        long double p = (a + b + c) / 2;
                        r = a * b * c / (4 * sqrt(p * (p - a) * (p - b) * (p - c)));
                
                        Line l1, l2;
                
                        l1.a = p1.x - p2.x;
                        l1.b = p1.y - p2.y;
                        l1.c = -l1.a * (p1.x + p2.x) / 2 - l1.b * (p1.y + p2.y) / 2;
                
                        l2.a = p1.x - p3.x;
                        l2.b = p1.y - p3.y;
                        l2.c = -l2.a * (p1.x + p3.x) / 2 - l2.b * (p1.y + p3.y) / 2;
                
                        o = l1.crossWithLine(l2).second;
                    }
                
                    pair<int, vector<point>> crossWithLine(Line l) {
                        long double a = l.a;
                        long double b = l.b;
                        long double c = l.c;
                
                        int status = 0;
                        vector<point> res(2);
                
                        c = c + a * o.x + b * o.y;
                
                        long double norm = a * a + b * b;
                
                        long double x = -c * a / norm;
                        long double y = -c * b / norm;
                
                        if (c * c - r * r * norm > EPS)
                            status = 0;
                        else if (abs(c * c - r * r * norm) < EPS) {
                            status = 1;
                            res[0] = point(x, y) + o;
                        }
                        else {
                            status = 2;
                            long double d = r * r - c * c / norm;
                            long double mult = sqrt(d / norm);
                
                            long double ax, ay, bx, by;
                            ax = x + b * mult;
                            ay = y - a * mult;
                            bx = x - b * mult;
                            by = y + a * mult;
                
                            res[0] = point(ax, ay) + o;
                            res[1] = point(bx, by) + o;
                        }
                
                        return { status, res };
                    }
                
                    pair<int, vector<point>> crossWithCycle(Cycle C) {
                        point o2 = C.o;
                        long double r2 = C.r;
                
                        int status = 0;
                        vector<point> res(2);
                
                        if (o == o2) {
                            if (r == r2)
                                status = 3;
                            else
                                status = 0;
                            return { status, res };
                        }
                
                        long double d = distPointToPoint(o, o2);
                
                        if (r + r2 < d || d < abs(r - r2)) {
                            status = 0;
                            return { status, res };
                        }
                
                        long double a = (r * r - r2 * r2 + d * d) / (2 * d);
                        long double b = d - a;
                        long double h = sqrt(r * r - a * a);
                
                        point p = o + (o2 - o) * (a / d);
                
                        res[0].x = p.x + (o2.y - o.y) / d * h;
                        res[0].y = p.y - (o2.x - o.x) / d * h;
                        res[1].x = p.x - (o2.y - o.y) / d * h;
                        res[1].y = p.y + (o2.x - o.x) / d * h;
                
                        if (res[0] == res[1])
                            status = 1;
                        else
                            status = 2;
                
                        return{ status, res };
                    }
                
                    long double lenArea(point a, point b) {
                        long double angle = getAngle(o, a, b);
                        return angle * r;
                    }
                };
                \s
                struct ang {
                    long double a, b;
                
                    bool operator ==(ang other) const {
                        return a == other.a && b == other.b;
                    }
                };
                
                bool operator <(const ang& l, const ang& r) {
                    if (l.b == 0 && r.b == 0)
                        return l.a < r.a;
                    return l.a * r.b < l.b * r.a;
                }
                
                bool cmp(point a, point b) {
                    if (a.x == b.x)
                        return a.y < b.y;
                    return a.x < b.x;
                }
                
                struct Polygon {
                    vector<point> pts;
                    int sz;
                
                    vector<ang> angs;
                    point zero;
                
                    vector<point> hull;
                    int szHull;
                
                    Polygon(vector<point> pts_) {
                        pts = pts_;
                        sz = (int)pts.size();
                    }
                
                    long double S() {
                        long double res = 0;
                
                        for (int i = 0; i < sz; i++) {
                            point a, b;
                            if (i)
                                a = pts[i - 1];
                            else
                                a = pts.back();
                            b = pts[i];
                
                            res += (a.x - b.x) * (a.y + b.y);
                        }
                        res = abs(res);
                        res /= 2;
                
                        return res;
                    }
                
                    long double P() {
                        long double res = 0;
                
                        for (int i = 0; i < sz; i++) {
                            point a, b;
                            if (i)
                                a = pts[i - 1];
                            else
                                a = pts.back();
                            b = pts[i];
                
                            res += distPointToPoint(a, b);
                        }
                
                        return res;
                    }
                
                    long double getSigned(point a, point b, point c) {
                        point ab = point(a, b);
                        point ac = point(a, c);
                        return ab * ac;
                    }
                
                    void buildAng() {
                        zero = pts[0];
                        int id = 0;
                        for (int i = 0; i < sz; i++) {
                            if (zero.x > pts[i].x || (zero.x == pts[i].x && zero.y > pts[i].y)) {
                                zero = pts[i];
                                id = i;
                            }
                        }
                
                        vector<point> tmp = pts;
                        rotate(tmp.begin(), tmp.begin() + id, tmp.end());
                        angs.resize(sz - 1);
                
                        for (int i = 1; i < sz; i++) {
                            int j = i - 1;
                            angs[j].a = tmp[i].y - zero.y;
                            angs[j].b = tmp[i].x - zero.x;
                            if (angs[j].a == 0)
                                angs[j].b = (angs[j].b < 0 ? -1 : 1);
                        }
                
                    }
                
                    bool pointOnPolygon(point pnt) {
                        if (angs.size() == 0)
                            buildAng();
                
                        bool res = 0;
                
                        ang angP;
                        angP.a = pnt.y - zero.y;
                        angP.b = pnt.x - zero.x;
                        if (angP.a == 0)
                            angP.b = (angP.b < 0 ? -1 : 1);
                
                        if (zero == pnt)
                            res = 1;
                        else {
                            auto it = upper_bound(angs.begin(), angs.end(), angP);
                            if (it == angs.end() && angP == angs.back())
                                it = angs.end() - 1;
                            if (it != angs.begin() && it != angs.end()) {
                                int id = it - angs.begin();
                                point p1 = pts[id];
                                point p2 = pts[id + 1];
                                if (getSigned(pnt, p1, p2) >= 0)
                                    res = 1;
                            }
                        }
                
                        return res;
                    }
                    \s
                    void buildHull() {
                        vector<point> tmp = pts;
                        sort(tmp.begin(), tmp.end(), cmp);
                        \s
                        point start = tmp[0];
                        point finish = tmp.back();
                
                        vector<point> up, down;
                
                        up.push_back(start);
                        down.push_back(start);
                
                        for (int i = 1; i < sz; i++) {
                        if (i == sz - 1 || getSigned(start, finish, tmp[i]) > 0) {
                        while (up.size() > 1 && getSigned(tmp[i], up[up.size() - 2], up[up.size() - 1]) >= 0)
                            up.pop_back();
                            up.push_back(tmp[i]);
                        }
                        if (i == sz - 1 || getSigned(start, finish, tmp[i]) < 0) {
                            while (down.size() > 1 && getSigned(tmp[i], down[down.size() - 1], down[down.size() - 2]) >= 0)
                                down.pop_back();
                                down.push_back(tmp[i]);
                            }
                        }
                
                        for (int i = 0; i < up.size(); i++) {
                            hull.push_back(up[i]);
                        }
                        for (int i = down.size() - 2; i >= 1; i--) {
                            hull.push_back(down[i]);
                        }
                
                        szHull = (int)hull.size();
                    }
                };
                """);
        options.put("BFS and DFS", """
                vector<int> mark;
                vector<vector<int>> graph;
                
                int n, m;
                
                void dfs(int v) {
                    mark[v] = 1;
                    for (auto u : graph[v]) {
                        if (!mark[u]) {
                            dfs(u);
                        }
                    }
                }
                
                void bfs(int s) {
                    queue<int> q;
                    q.push(s);
                    mark[s] = 1;
                
                    while (!q.empty()) {
                        int v = q.front();
                        q.pop();
                        for (auto u : graph[v]) {
                            if (!mark[u]) {
                                mark[u] = 1;
                                q.push(u);
                            }
                        }
                    }
                }
                """);
        options.put("Dijkstra algorithm", """
                vector<int> mark;
                vector<vector<pair<int, int>>> graph;
                
                int n, m;
                
                vector<int> dijkstra(int s) {
                    vector<int> d(n, 1e9 + 7);
                    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> q;
                    d[s] = 0;
                    q.push({ 0, s });
                
                    while (!q.empty()) {
                        pair<int, int> x = q.top();
                        int cur_d = x.first;
                        int v = x.second;
                        q.pop();
                
                        if (cur_d > d[v])
                            continue;
                
                        for (pair<int, int> y : graph[v]) {
                            int w = y.first;
                            int u = y.second;
                            if (d[u] > d[v] + w) {
                                d[u] = d[v] + w;
                                q.push({ d[u], u });
                            }
                        }
                    }
                
                    return d;
                }
                """);
        options.put("Segment tree with addition on the segment", """
                struct Node {
                    int maxima;
                    int add;
                };
                
                vector<Node> tree;
                vector<int> start_v;
                
                void apply(int v, int l, int r, int x) {
                    tree[v].maxima += x;
                    tree[v].add += x;
                }
                
                void push(int v, int l, int r) {
                    if (l + 1 == r) {
                        tree[v].add = 0;
                        return;
                    }
                    int mid = (l + r) / 2;
                    apply(2 * v + 1, l, mid, tree[v].add);
                    apply(2 * v + 2, mid, r, tree[v].add);
                    tree[v].add = 0;
                }
                
                Node merge(Node a, Node b) {
                    int maxima = max(a.maxima, b.maxima);
                    return { maxima, 0 };
                }
                
                void build(int v, int l, int r) {
                    if (l + 1 == r) {
                        tree[v].maxima = start_v[l];
                        return;
                    }
                    int mid = (l + r) / 2;
                    build(2 * v + 1, l, mid);
                    build(2 * v + 2, mid, r);
                    tree[v] = merge(tree[2 * v + 1], tree[2 * v + 2]);
                }
                
                void upd(int v, int l, int r, int ql, int qr, int x) {
                    if (ql >= r || qr <= l)
                        return;
                    if (ql <= l && qr >= r) {
                        apply(v, l, r, x);
                        return;
                    }
                    push(v, l, r);
                    int mid = (l + r) / 2;
                    upd(2 * v + 1, l, mid, ql, qr, x);
                    upd(2 * v + 2, mid, r, ql, qr, x);
                    tree[v] = merge(tree[2 * v + 1], tree[2 * v + 2]);
                }
                
                Node get_ans(int v, int l, int r, int ql, int qr) {
                    if (ql >= r || qr <= l)
                        return { 0, 0 };
                    if (ql <= l && qr >= r)
                        return tree[v];
                    push(v, l, r);
                    int mid = (l + r) / 2;
                    return merge(get_ans(2 * v + 1, l, mid, ql, qr),
                        get_ans(2 * v + 2, mid, r, ql, qr));
                }
                """);
        options.put("Segment tree with scanline", """
                struct Node {
                     int val, step;
                 };
                
                 vector<Node> tree;
                 vector<int> push;
                 vector<int> vec;
                
                 struct Event {
                     int x, y1, y2, type;
                 };
                
                 bool cmp(Event a, Event b) {
                     return a.x < b.x;
                 }
                
                 Node merge(Node a, Node b) {
                     if (a.val == b.val)
                         return { a.val, a.step + b.step };
                     if (a.val < b.val)
                         return a;
                     else
                         return b;
                 }
                
                 void makePush(int v) {
                     if (push[v] == 0)
                         return;
                
                     push[2 * v + 1] += push[v];
                     push[2 * v + 2] += push[v];
                
                     tree[2 * v + 1].val += push[v];
                     tree[2 * v + 2].val += push[v];
                
                     push[v] = 0;
                 }
                
                 void build(int v, int l, int r) {
                     if (l == r - 1) {
                         tree[v] = {0, vec[l] };
                         return;
                     }
                     int mid = (l + r) / 2;
                     build(2 * v + 1, l, mid);
                     build(2 * v + 2, mid, r);
                     tree[v] = merge(tree[2 * v + 1], tree[2 * v + 2]);
                 }
                
                 void upd(int v, int l, int r, int ql, int qr, int x) {
                     if (qr <= l || ql >= r)
                         return;
                     if (ql <= l && qr >= r) {
                         tree[v].val += x;
                         push[v] += x;
                         return;
                     }
                
                     makePush(v);
                     int mid = (l + r) / 2;
                     upd(2 * v + 1, l, mid, ql, qr, x);
                     upd(2 * v + 2, mid, r, ql, qr, x);
                     tree[v] = merge(tree[2 * v + 1], tree[2 * v + 2]);
                 }
                """);
        options.put("Z/pref-functions", """
                vector<int> ZFunction(string& s) {
                    int n = (int)s.size();
                    int l = 0, r = 0;
                    vector<int> z(n);
                    z[0] = n;
                    for (int i = 1; i < n; i++) {
                        if (i < r)
                            z[i] = min(z[i - l], r - i);
                        while (z[i] + i < n && s[z[i]] == s[z[i] + i])
                            z[i]++;
                        if (z[i] + i > r) {
                            l = i;
                            r = z[i] + i;
                        }
                    }
                
                    return z;
                }
                
                vector<int> PrefFunction(string& s) {
                    int n = (int)s.size();
                    int k = 0;
                    vector<int> pref(n);
                    for (int i = 1; i < n; i++) {
                        k = pref[i - 1];
                        while (k > 0 && s[i] != s[k])
                            k = pref[k - 1];
                        if (s[i] == s[k])
                            k++;
                        pref[i] = k;
                    }
                
                    return pref;
                }
                """);
        options.put("Bitwise pref tree with search for y that x xor y is max", """
                struct Node {
                    int go[2];
                    int cnt;
                    int tree_cnt;
                    Node() {
                        go[0] = go[1] = -1;
                        cnt = 0;
                        tree_cnt = 0;
                    }
                };
                
                vector<Node> trie;
                
                void add_val(int x) {
                    int v = 0;
                    for (int i = 31; i >= 0; i--) {
                        bool flag = (x & (1 << i));
                        if (trie[v].go[flag] == -1) {
                            trie.emplace_back();
                            trie[v].go[flag] = trie.size() - 1;
                        }
                        trie[v].tree_cnt++;
                        v = trie[v].go[flag];
                    }
                    trie[v].tree_cnt++;
                    trie[v].cnt++;
                }
                
                void del_val(int x) {
                    int v = 0;
                    for (int i = 31; i >= 0; i--) {
                        bool flag = (x & (1 << i));
                        trie[v].tree_cnt--;
                        v = trie[v].go[flag];
                    }
                    trie[v].tree_cnt--;
                    trie[v].cnt--;
                }
                
                // In this case, for a given x, it finds such a y that (x XOR y) will be maximized.
                int get_ans(int x) {
                    int v = 0;
                    int res = 0;
                    for (int i = 31; i >= 0; i--) {
                        bool flag = (x & (1 << i));
                        if (trie[v].go[!flag] != -1 && trie[trie[v].go[!flag]].tree_cnt > 0) {
                            v = trie[v].go[!flag];
                            res += (1 << i);
                        }
                        else {
                            if (trie[v].go[flag] != -1)
                                v = trie[v].go[flag];
                            else
                                break;
                        }
                    }
                    return res;
                }
                """);
        options.put("Pref tree with search for k-th string", """
                struct node {
                    int cnt;
                    int go[26];
                    int tree_cnt;
                    node() {
                        cnt = 0;
                        tree_cnt = 0;
                        for (int i = 0; i < 26; i++) {
                            go[i] = -1;
                        }
                    }
                };
                
                vector<node> trie;
                
                void add_str(string s) {
                    int v = 0;
                    for (int i = 0; i < (int)s.size(); i++) {
                        if (trie[v].go[s[i] - 'a'] == -1) {
                            trie.emplace_back();
                            trie[v].go[s[i] - 'a'] = (int)trie.size() - 1;
                        }
                        trie[v].tree_cnt++;
                        v = trie[v].go[s[i] - 'a'];
                    }
                    trie[v].tree_cnt++;
                    trie[v].cnt++;
                }
                
                // In this case find kth string
                void find_str(int k) {
                    int v = 0;
                    string ans;
                    int helper = 0;
                    while (1) {
                        if (helper == k - 1 && trie[v].cnt) {
                            cout << ans << "\\n";
                            return;
                        }
                        for (int i = 0; i < 26; i++) {
                            if (trie[v].go[i] != -1) {
                                helper += trie[trie[v].go[i]].tree_cnt;
                                if (helper == k) {
                                    v = trie[v].go[i];
                                    ans += 'a' + i;
                                    int flag = 1;
                                    while (flag) {
                                        flag = 0;
                                        for (int j = 25; j >= 0; j--) {
                                            if (trie[v].go[j] != -1) {
                                                flag = 1;
                                                ans += 'a' + j;
                                                v = trie[v].go[j];
                                                break;
                                            }
                                        }
                                    }
                                    cout << ans << "\\n";
                                    return;
                                }
                                if (helper > k) {
                                    helper -= trie[trie[v].go[i]].tree_cnt;
                                    v = trie[v].go[i];
                                    ans += 'a' + i;
                                    break;
                                }
                            }
                        }
                    }
                }
                """);
        options.put("LCA", """
                const int L = 20;
                
                int n, m;
                vector<vector<int>> graph;
                vector<vector<int>> jmp;
                vector<int> dist;
                
                void countDist(int v, int p) {
                    jmp[v][0] = p;
                    for (int u : graph[v]) {
                        if (u == p)
                            continue;
                
                        dist[u] = dist[v] + 1;
                        countDist(u, v);
                    }
                }
                
                int lca(int u, int v) {
                    if (dist[u] < dist[v])
                        swap(u, v);
                
                    for (int i = L; i >= 0; i--) {
                        int u2 = jmp[u][i];
                        if (dist[u2] >= dist[v])
                            u = u2;
                    }
                
                    if (u == v) {
                        return v;
                    }
                
                    for (int i = L; i >= 0; i--) {
                        int u2 = jmp[u][i];
                        int v2 = jmp[v][i];
                        if (u2 != v2) {
                            u = u2;
                            v = v2;
                        }
                    }
                
                    return jmp[u][0];
                }
                """);
        return options;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        Editor editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        Document doc = editor.getDocument();

        Map<String, String> options = getOptions();

        JBTextField searchField = new JBTextField();
        searchField.setPreferredSize(JBUI.size(200, 30));

        JBList<String> list = new JBList<>(options.keySet());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filter = searchField.getText().toLowerCase();
                List<String> filteredOptions = new ArrayList<>();
                for (String option : options.keySet()) {
                    if (option.toLowerCase().contains(filter)) {
                        filteredOptions.add(option);
                    }
                }
                list.setListData(filteredOptions.toArray(new String[0]));
            }
        });

        PopupChooserBuilder<String> popupBuilder = new PopupChooserBuilder<>(list);
        popupBuilder.setTitle("Choose Algorithm or Structure")
                .setMovable(true)
                .setResizable(true)
                .setNorthComponent(searchField).setItemChosenCallback(() -> {
                    String selectedOption = list.getSelectedValue();
                    if (selectedOption != null) {
                        String textToInsert = options.get(selectedOption);
                        if (textToInsert != null) {
                            WriteCommandAction.runWriteCommandAction(project, () -> doc.insertString(editor.getCaretModel().getOffset(), textToInsert));
                        }
                    }
                });

        popupBuilder.createPopup().showInFocusCenter();
    }
}
